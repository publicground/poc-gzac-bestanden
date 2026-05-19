namespace="$1"
dbhost="$2"
dbname="$3"
dbuser="$4"
dbpassword="$5"

# Generate in this script
pgpass="$dbhost:5432:$dbname:$dbuser:$dbpassword"
jobname="dbbackup-$dbname-job"
backupfile=/var/backups/$dbname-backup-$(date +"%Y-%m-%d-%H-%M").sql
storageclassenv=${namespace:4:1}

echo "Backupfile to be created: $backupfile for job $jobname"

kubectl apply -n $namespace -f - << YAML
      apiVersion: v1
      kind: PersistentVolumeClaim
      metadata:
        annotations:
          volume.beta.kubernetes.io/storage-provisioner: file.csi.azure.com
          volume.kubernetes.io/storage-provisioner: file.csi.azure.com
        labels:
          app.kubernetes.io/instance: dbbackup
          app.kubernetes.io/name: dbbackup
        name: dbbackup
        namespace: $namespace
      spec:
        accessModes:
        - ReadWriteMany
        resources:
          requests:
            storage: 200Gi
        storageClassName: zgw-$storageclassenv-azurefile-csi
        volumeMode: Filesystem
YAML

if [ "$namespace" = "zgw-o" ]; then
read -r -d '' tolerations << YAML
tolerations:
            - effect: NoSchedule
              key: zgwo01
              operator: Equal
              value: 'true'
YAML

  echo "$tolerations"
fi

# read -r -d '' k8sjob <<YAML
kubectl apply -n $namespace -f - << YAML
      apiVersion: batch/v1
      kind: Job
      metadata:
        name: $jobname
      spec:
        completions: 1
        ttlSecondsAfterFinished: 3600
        template:
          metadata:
            name: $jobname
          spec:
            containers:
            - name: postgres-backup
              image: crzgwpweu01.azurecr.io/postgres:12
              command: ["/bin/sh"]
              args: ["-c", 'echo "$pgpass" > /root/.pgpass && chmod 600 /root/.pgpass && pg_dump -U $dbuser -h $dbhost $dbname > $backupfile']
              volumeMounts:
              - mountPath: /var/backups
                name: data
              resources:
                limits:
                  cpu: 1
                  memory: "1000Mi"
                requests:
                  cpu: 500m
                  memory: "500Mi"
            restartPolicy: Never
            volumes:
            - name: data
              persistentVolumeClaim:
                claimName: dbbackup
            $tolerations
YAML
# echo "$k8sjob"
kubectl wait --for condition="complete" job/$jobname -n $namespace --timeout=1800s

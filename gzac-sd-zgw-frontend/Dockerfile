FROM nginxinc/nginx-unprivileged:1.30-alpine@sha256:442753882674b49ae2c1de83ed67896131c0777f56df5005e356e62bc3f7e7ce

WORKDIR /usr/share/nginx/html

COPY /conf/default.conf /etc/nginx/conf.d/default.conf
COPY --chown=1000:1000 /deployment/dist/ .

RUN \
    sed -i.bak 's/^user/#user/' /etc/nginx/nginx.conf && \
    sed -i.bak 's/listen\(.*\)80;/listen 8080;/' /etc/nginx/conf.d/default.conf

USER 1000

CMD ["/bin/sh",  "-c",  "exec nginx -g 'daemon off;'"]

EXPOSE 8080

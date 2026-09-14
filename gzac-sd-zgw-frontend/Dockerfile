FROM nginxinc/nginx-unprivileged:1.30-alpine@sha256:adf5042a17f4ecdd200c595fa9ffd1be37efb18f89a830bd1a00e4ab4d59d42c

WORKDIR /usr/share/nginx/html

COPY /conf/default.conf /etc/nginx/conf.d/default.conf
COPY --chown=1000:1000 /deployment/dist/ .

RUN \
    sed -i.bak 's/^user/#user/' /etc/nginx/nginx.conf && \
    sed -i.bak 's/listen\(.*\)80;/listen 8080;/' /etc/nginx/conf.d/default.conf

USER 1000

CMD ["/bin/sh",  "-c",  "exec nginx -g 'daemon off;'"]

EXPOSE 8080

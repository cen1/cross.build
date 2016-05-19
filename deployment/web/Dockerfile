FROM debian:jessie

RUN apt-get update && \
apt-get install -y build-essential && \
apt-get install -y curl && \
apt-get install -y sudo && \
apt-get install -y git

RUN curl -sL https://deb.nodesource.com/setup_4.x | sudo -E bash - && \
apt-get install -y nodejs

RUN mkdir /var/www && cd /var/www && \
git clone https://github.com/cen1/cross.build.git

EXPOSE 3002

WORKDIR /var/www/cross.build/web
RUN npm install && npm install -g grunt-cli bower

ENV APIURL http://localhost:8080/cross-build-rest/v1
ENV KEYCLOAKURL http://localhost:8080/auth
ENV KEYCLOAKKEY changeme

CMD ["grunt", "serve"]
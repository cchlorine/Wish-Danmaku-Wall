FROM node:14.19-alpine AS builder
RUN set -eux && sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
WORKDIR /frontend
COPY ./frontend /frontend
RUN npm config set registry https://registry.npmmirror.com
RUN apk add python2 
RUN apk add --no-cache --virtual .build-deps alpine-sdk zlib autoconf
RUN npm install
RUN apk del .build-deps
RUN npm run build

FROM nginx
COPY --from=builder /frontend/dist /var/www/wish_dist
EXPOSE 80 443
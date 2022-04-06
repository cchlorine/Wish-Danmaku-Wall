FROM node:14.19-alpine AS builder
RUN set -eux && sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
WORKDIR /frontend
COPY ./frontend /frontend
RUN apk add python2
RUN apk --no-cache add \
  ca-certificates \
  build-base \
  autoconf \
  automake \
  zlib \
  bash \
  libltdl \
  libtool \
  zlib-dev \
  nasm
ENV ACLOCAL_PATH=/usr/share/aclocal
ENV LIBRARY_PATH=/lib:/usr/lib
RUN npm install
RUN npm run build

FROM nginx
COPY --from=builder /frontend/dist /var/www/wish_dist
EXPOSE 80 443
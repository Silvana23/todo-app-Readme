FROM node:22.0.0-alpine3.19

ARG VITE_API_URL
ENV VITE_API_URL=$VITE_API_URL

WORKDIR /app

COPY . .

RUN ["npm", "install"]
RUN ["chmod", "+x", "./run.sh"]

EXPOSE 8080
CMD ["./run.sh"]
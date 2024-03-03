FROM node:14-alpine
COPY . .
RUN npm ci
EXPOSE 8081
ENTRYPOINT ["npm", "start"]

FROM --platform=linux/amd64 node:18-alpine

WORKDIR /app

COPY public/ /app/public
COPY src/ /app/src
COPY package.json /app/

RUN npm install

# Set environment variables
ENV REACT_APP_STRIPE_PUBLIC_KEY = ""
ENV REACT_APP_MAP_KEY = ""
ENV REACT_APP_BASE_URL = "http://csci5308vm9.research.cs.dal.ca:8080/api"

EXPOSE 3000

CMD ["npm", "start"]
FROM --platform=linux/amd64 node:18-alpine

WORKDIR /app

COPY public/ /app/public
COPY src/ /app/src
COPY package.json /app/

RUN npm install

# Set environment variables
ENV REACT_APP_STRIPE_PUBLIC_KEY = "pk_test_51J0PUySIJjtkSpgkooDHynUUv6NkOpxarspwxKuhx4ZwNQYx0autnl95jEh2MN5jbmlTDsme1fCf8N65IieDV6LO00YHVh6PS0"
ENV REACT_APP_MAP_KEY = "AIzaSyC92l4kh5h0HvZxwjtRg_F_uIwDCphriQI"
ENV REACT_APP_BASE_URL = "http://csci5308vm9.research.cs.dal.ca:8080/api"

EXPOSE 3000

CMD ["npm", "start"]
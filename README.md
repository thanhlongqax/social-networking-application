````markdown
# 🌐 Social Networking Application

A full-featured social networking platform built with **Spring Boot**, supporting real-time messaging, notifications, file uploads, video calls, and intelligent recommendations.

## 🚀 Overview

This application is a distributed microservices-based **social networking system** designed with scalability, modularity, and performance in mind. It integrates various technologies such as **Kafka**, **Elasticsearch**, **Socket.IO**, and **Cloudinary**, while leveraging **Spring Cloud Gateway** and **Eureka Server** for service discovery and routing.

---

## 🧩 System Architecture

```bash
PORT | SERVICE NAME
-----|------------------------------
8001 | API Gateway (Spring Cloud Gateway)
8002 | Eureka Server (Service Registry)
8003 | Auth Service
8004 | File Service (Cloudinary integration)
8005 | Follower Service
8006 | User Service
8007 | Interaction Service (Likes, Comments)
8008 | NewFeeds Service (Posts & Feed Aggregation)
8009 | Notification Service (Kafka-based)
8010 | Message Service (Socket.IO)
8011 | Text Categorization Service (ML/NLP)
8012 | Recommendation Service (User/Product-based)
8013 | Video Call Service (WebRTC integration)
8014 | Search Service (Elasticsearch)
8014 | Friend Recommendation Service
````

> ⚠️ `Search Service` and `Friend Recommendation Service` currently share port `8014`. Consider changing one for production use.

---

## 🛠️ Technologies Used

| Category          | Technology                     |
| ----------------- | ------------------------------ |
| Language          | Java (Spring Boot)             |
| Gateway           | Spring Cloud Gateway           |
| Service Discovery | Eureka Server                  |
| Messaging         | Apache Kafka                   |
| Real-time         | Socket.IO (WebSocket for Chat) |
| File Storage      | Cloudinary                     |
| Search Engine     | Elasticsearch                  |
| API Communication | RESTful API, Feign Client      |
| Build Tool        | Maven                          |
| Authentication    | JWT-based Authentication       |
| Others            | Lombok, Spring Security        |

---

## 🔧 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/social-networking-application.git
cd social-networking-application
```

### 2. Prerequisites

* Java 17+
* Docker & Docker Compose (for Kafka, Elasticsearch, etc.)
* Maven
* Cloudinary account (for image/video storage)
* Redis (optional for caching, tokens, etc.)

### 3. Run Core Services

```bash
# Start Eureka Server
cd eureka-server
mvn spring-boot:run

# Start API Gateway
cd gateway
mvn spring-boot:run
```

Repeat for each microservice:

```bash
cd <service-name>
mvn spring-boot:run
```

> Alternatively, use Docker Compose to orchestrate services (recommended for production or testing).

---

## 🔄 Message Queue Integration

Kafka is used for:

* **Notification Service** (new followers, likes, comments, etc.)
* **Activity Logs**
* **Real-time Feed Generation**

---

## 📁 File Storage

File uploads (images/videos) are managed using **Cloudinary**. Configure your `.env` or `application.yml` with:

```yaml
cloudinary:
  cloud_name: your_cloud_name
  api_key: your_api_key
  api_secret: your_api_secret
```

---

## 📡 Real-time Messaging

The **Message Service** uses `Socket.IO` for real-time chat between users.

* Supports private chats
* Supports typing indicators and message status
* Optimized with Redis Pub/Sub (optional)

---

## 🔍 Search Features

**Search Service** uses **Elasticsearch** to index and search:

* User profiles
* Posts
* Hashtags and mentions

---

## 🧠 Friend & Content Recommendations

* **Text Categorization Service**: Classifies post content by topic
* **Recommendation Service**: Suggests content based on user interaction
* **Friend Recommendation Service**: Based on mutual friends, interests, and interactions

---

## 📞 Video Calling

**Video Call Service** supports 1:1 video calls using **WebRTC** and signaling over WebSocket. (unexecuted) 

---

## 🧪 Testing

Each service contains unit and integration tests using:

* JUnit 5
* Mockito
* TestContainers (Kafka, Elasticsearch)

---

## 📊 Monitoring & Logs

* Centralized logging with ELK (Elastic, Kibana , )
* Service metrics exposed via Actuator endpoints

---

## 📌 Contribution

We welcome contributions! Please:

1. Fork the repo
2. Create your feature branch (`git checkout -b feature/xyz`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature/xyz`)
5. Create a Pull Request

---

## 📃 License

Distributed under the MIT License. See `LICENSE` for more information.

---

## 📬 Contact

For questions or feedback, please contact:

* Developer: \[Thanh Long]
* Email: \[[thanhklongndp@gmail.com])]
* LinkedIn: \[[https://linkedin.com/in/yourprofile](https://linkedin.com/in/yourprofile)]

---

## 📸 Screenshots (Optional)


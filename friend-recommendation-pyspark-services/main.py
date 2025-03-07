from flask import Flask, request, jsonify
from pyspark.sql import SparkSession
from pyspark.ml.recommendation import ALS
from pyspark.ml.feature import Word2Vec
from flask import Flask, request, jsonify
from pyspark.sql import SparkSession
from pyspark.ml.recommendation import ALS
from pyspark.sql.functions import col, from_json
from pyspark.sql.types import StructType, StructField, StringType, IntegerType
from kafka import KafkaConsumer, KafkaProducer
import json
from flasgger import Swagger
import json
import logging
import time
# Cấu hình logging
logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

# Tạo Flask App
app = Flask(__name__)
swagger = Swagger(app)
# Tạo Spark Session
spark = SparkSession.builder.appName("PySparkService").getOrCreate()

# Kafka Config
KAFKA_BROKER = "broker:29092"

TOPIC_INTERACTION = "user_interactions"
TOPIC_INPUT = "user_interests"

TOPIC_OUTPUT = "friend_recommendations"

import json
import logging
import time
from kafka import KafkaConsumer, KafkaProducer, errors

# Cấu hình logging
logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

# Địa chỉ Kafka
KAFKA_BROKER = "broker:29092"

# Các topic
TOPIC_INTERACTION = "user_interactions"
TOPIC_INPUT = "user_interests"
TOPIC_OUTPUT = "friend_recommendations"

# Hàm kiểm tra Kafka có sẵn sàng hay không
def wait_for_kafka(broker, retries=10, delay=5):
    for i in range(retries):
        try:
            logging.info(f"🔄 Thử kết nối Kafka lần {i+1}...")
            consumer = KafkaConsumer(bootstrap_servers=broker)
            consumer.close()
            logging.info("✅ Kafka đã sẵn sàng!")
            return
        except errors.NoBrokersAvailable:
            logging.warning(f"❌ Kafka chưa sẵn sàng, thử lại sau {delay}s...")
            time.sleep(delay)
    logging.error("🚨 Không thể kết nối Kafka sau nhiều lần thử! Kiểm tra lại Kafka Broker.")
    exit(1)

# Kiểm tra Kafka trước khi khởi tạo consumer và producer
wait_for_kafka(KAFKA_BROKER)

# Kafka Consumer cho user interactions
try:
    consumer = KafkaConsumer(
        TOPIC_INTERACTION,
        bootstrap_servers=KAFKA_BROKER,
        value_deserializer=lambda x: json.loads(x.decode('utf-8')),
        auto_offset_reset='earliest',
        enable_auto_commit=True,
        group_id='friend-recommendation-group'
    )
    logging.info(f"✅ Consumer kết nối tới topic: {TOPIC_INTERACTION}")
except Exception as e:
    logging.error(f"🚨 Lỗi khi tạo KafkaConsumer cho {TOPIC_INTERACTION}: {e}")
    exit(1)

# Kafka Consumer cho user interests
try:
    consumer_interests = KafkaConsumer(
        TOPIC_INPUT,
        bootstrap_servers=KAFKA_BROKER,
        value_deserializer=lambda x: json.loads(x.decode('utf-8')),
        auto_offset_reset='earliest',
        enable_auto_commit=True,
        group_id='friend-recommendation-group'
    )
    logging.info(f"✅ Consumer kết nối tới topic: {TOPIC_INPUT}")
except Exception as e:
    logging.error(f"🚨 Lỗi khi tạo KafkaConsumer cho {TOPIC_INPUT}: {e}")
    exit(1)

# Kafka Producer
try:
    producer = KafkaProducer(
        bootstrap_servers=KAFKA_BROKER,
        value_serializer=lambda x: json.dumps(x).encode('utf-8')
    )
    logging.info("✅ KafkaProducer đã khởi tạo thành công!")
except Exception as e:
    logging.error(f"🚨 Lỗi khi tạo KafkaProducer: {e}")
    exit(1)

# Kiểm tra Consumer có nhận được tin nhắn không
logging.info("🎯 Đang lắng nghe tin nhắn từ Kafka...")

for message in consumer:
    logging.info(f"📩 Nhận tin nhắn từ {TOPIC_INTERACTION}: {message.value}")


# Schema dữ liệu đầu vào từ Kafka
schema = StructType([
    StructField("userId", StringType(), True),
    StructField("targetUserId", StringType(), True),
    StructField("likes", IntegerType(), True),
    StructField("comments", IntegerType(), True),
    StructField("follows", IntegerType(), True),
    StructField("messages", IntegerType(), True)
])
# Schema cho sở thích người dùng
interest_schema = StructType([
    StructField("userId", StringType(), True),
    StructField("interests", StringType(), True)  # Lưu danh sách sở thích dưới dạng chuỗi JSON
])

def train_content_based_filtering():
    user_interests_data = []

    for msg in consumer_interests:
        user_interests_data.append(msg.value)

    if not user_interests_data:
        return None

    df = spark.createDataFrame(user_interests_data, schema=interest_schema)

    # Tách danh sách sở thích từ JSON
    df = df.withColumn("interests", from_json(col("interests"), ArrayType(StringType())))

    # Word2Vec để chuyển sở thích thành vector
    word2vec = Word2Vec(vectorSize=10, minCount=1, inputCol="interests", outputCol="interestVector")
    model = word2vec.fit(df)
    df = model.transform(df)

    return df.select("userId", "interestVector")
def train_collaborative_filtering():
    user_interactions_data = []
    for msg in consumer_interactions:
        user_interactions_data.append(msg.value)

    if not user_interactions_data:
        return None

    df = spark.createDataFrame(user_interactions_data, schema=interaction_schema)

    # Tính điểm tương tác
    df = df.withColumn("interactionScore", col("likes")*2 + col("comments")*3 + col("follows")*5 + col("messages")*10)

    # Huấn luyện mô hình ALS
    als = ALS(userCol="userId", itemCol="targetUserId", ratingCol="interactionScore", coldStartStrategy="drop")
    model = als.fit(df)

    recommendations = model.recommendForAllUsers(10)

    return recommendations
def hybrid_recommendations():
    cbf_result = train_content_based_filtering()
    cf_result = train_collaborative_filtering()

    if cbf_result is None and cf_result is None:
        return jsonify({"status": "error", "message": "No data available"})

    if cf_result is not None:
        cf_result = cf_result.toPandas()

    if cbf_result is not None:
        cbf_result = cbf_result.toPandas()

    # Kết hợp hai kết quả (ưu tiên người có điểm cao từ cả 2 phương pháp)
    hybrid_scores = {}

    if cf_result is not None:
        for _, row in cf_result.iterrows():
            user_id = row["userId"]
            for rec in row["recommendations"]:
                target_id = rec["targetUserId"]
                score = rec["rating"]
                hybrid_scores[(user_id, target_id)] = hybrid_scores.get((user_id, target_id), 0) + score

    if cbf_result is not None:
        for _, row in cbf_result.iterrows():
            user_id = row["userId"]
            for _, rec_row in cbf_result.iterrows():
                target_id = rec_row["userId"]
                if user_id != target_id:
                    score = sum(row["interestVector"] * rec_row["interestVector"])  # Tích vô hướng
                    hybrid_scores[(user_id, target_id)] = hybrid_scores.get((user_id, target_id), 0) + score

    # Chuyển kết quả thành danh sách gợi ý
    recommendations = {}
    for (user_id, target_id), score in sorted(hybrid_scores.items(), key=lambda x: -x[1])[:5]:
        if user_id not in recommendations:
            recommendations[user_id] = []
        recommendations[user_id].append({"recommendedUserId": target_id, "score": score})

    # Gửi kết quả về Kafka
    for user_id, recs in recommendations.items():
        producer.send(TOPIC_OUTPUT, {"userId": user_id, "recommendations": recs})

    return jsonify({"status": "success", "message": "Hybrid recommendations generated and sent to Kafka."})

@app.route('/health', methods=['GET'])
def health_check():
    """
    Kiểm tra tình trạng service
    ---
    responses:
      200:
        description: Service hoạt động bình thường
    """
    return jsonify({"status": "running"}), 200

@app.route("/train/hybrid_recommendations", methods=["POST"])
def train_hybrid():
    """
    Kết hợp Content-Based Filtering và Collaborative Filtering để tạo danh sách gợi ý
    ---
    responses:
      200:
        description: Thành công
    """
    return hybrid_recommendations()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001 ,debug=True)

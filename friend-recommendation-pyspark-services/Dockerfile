FROM python:3.9

# Cài đặt Java (cần thiết cho Spark)
RUN apt-get update && apt-get install -y openjdk-17-jdk && \
    rm -rf /var/lib/apt/lists/*  # Xóa cache để giảm kích thước image

# Thiết lập biến môi trường cho Java
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

# Cài đặt các thư viện Python cần thiết
RUN pip install --no-cache-dir pyspark kafka-python flask numpy pandas flasgger

# Đặt thư mục làm việc
WORKDIR /app

# Copy toàn bộ mã nguồn vào container
COPY . .

# Cấu hình biến môi trường cho Flask
ENV FLASK_APP=app.py
ENV FLASK_RUN_HOST=0.0.0.0
ENV FLASK_RUN_PORT=5001

# Mở cổng 5001
EXPOSE 5001

# Chạy ứng dụng
CMD ["python", "main.py"]


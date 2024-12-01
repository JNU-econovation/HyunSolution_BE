# Step 1: Use the base image
FROM openjdk:17-jdk

# Step 2: Set environment variables
ENV TZ=Asia/Seoul
ENV LANG=C.UTF-8

# Step 3: Manually set the timezone
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime \
    && echo "Asia/Seoul" > /etc/timezone

# Step 4: Expose application port
EXPOSE 8080

# Step 5: Copy application JAR file
COPY build/libs/dangu-0.0.1-SNAPSHOT.jar app.jar

COPY nginx/app.conf /etc/nginx/nginx.conf

# Step 6: Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]

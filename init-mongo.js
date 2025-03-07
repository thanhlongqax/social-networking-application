db = db.getSiblingDB('notification-service'); // Chọn database
// db.createCollection('notifications');         // Tạo collection

// db = db.getSiblingDB('user-service');         // Chọn database khác
// db.createCollection('users');                 // Tạo collection

db = db.getSiblingDB('message-service');         // Tạo thêm database
// db.createCollection('messages');              // Tạo collection

db = db.getSiblingDB('interaction-service');         // Tạo thêm database
// db.createCollection('reactions');
// db.createCollection('comments');

db = db.getSiblingDB('newsfeed-service');

db = db.getSiblingDB('follower-service');  // Tạo thêm database
// db.createCollection('bannedWord');
// db.createCollection('posts');
// db.createCollection('PostShare');
// db.createCollection('reports');
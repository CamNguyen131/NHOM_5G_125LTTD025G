package com.example.uyen_ck.models;

import java.util.List;

public class Chat {
    private String chatId;
    private List<String> participants; // Danh sách ID người tham gia (VD: ["user_01", "user_03"])
    private String lastMessage;        // Tin nhắn cuối cùng để hiện ở màn hình danh sách
    private long lastTimestamp;        // Thời gian nhắn tin cuối cùng

    // 1. Constructor rỗng (BẮT BUỘC ĐỂ FIREBASE HOẠT ĐỘNG)
    public Chat() {
    }

    // 2. Constructor đầy đủ (Để bạn dễ tạo object mới khi cần)
    public Chat(String chatId, List<String> participants, String lastMessage, long lastTimestamp) {
        this.chatId = chatId;
        this.participants = participants;
        this.lastMessage = lastMessage;
        this.lastTimestamp = lastTimestamp;
    }

    // 3. Getter và Setter (BẮT BUỘC ĐỂ LẤY DỮ LIỆU RA)
    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }
}
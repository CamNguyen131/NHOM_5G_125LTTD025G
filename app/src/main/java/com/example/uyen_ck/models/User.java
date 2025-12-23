package com.example.uyen_ck.models;

import java.util.List;
import java.util.Map;

public class User {
    private String uid;
    private String displayName; //
    private String email;
    private String phoneNumber;
    private String role; // "buyer" hoặc "seller"
    private String avatarUrl;
    private Map<String, Integer> stats;
    private List<Map<String, Object>> addresses;
    private long createdAt;
    public User() {}
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public Map<String, Integer> getStats() { return stats; }
    public void setStats(Map<String, Integer> stats) { this.stats = stats; }
    public List<Map<String, Object>> getAddresses() { return addresses; }
    public void setAddresses(List<Map<String, Object>> addresses) { this.addresses = addresses; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
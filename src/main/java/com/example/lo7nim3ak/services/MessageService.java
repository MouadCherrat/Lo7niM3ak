package com.example.lo7nim3ak.services;

import com.example.lo7nim3ak.dto.MessageDto;
import com.example.lo7nim3ak.dto.UserDto;
import com.example.lo7nim3ak.entities.Message;
import com.example.lo7nim3ak.entities.User;
import com.example.lo7nim3ak.repository.MessageRepository;
import com.example.lo7nim3ak.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;


    public Message sendMessage(Long senderId, Long receiverId, String content) {
        Message message = new Message();
        message.setSender(new User(senderId));
        message.setReceiver(new User(receiverId));
        message.setContent(content);
        message.setTimestamp(new Date());
        message.setIsRead(false);
        return messageRepository.save(message);
    }

    public List<MessageDto> getConversation(Long userId1, Long userId2) {
        List<Message> messages = messageRepository.findMessagesBetweenUsers(userId1, userId2);
        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markMessagesAsRead(Long senderId, Long receiverId) {
        List<Message> unreadMessages = messageRepository.findUnreadMessagesBetweenUsers(senderId, receiverId);
        unreadMessages.forEach(message -> message.setIsRead(true));
        messageRepository.saveAll(unreadMessages);
    }

    private MessageDto convertToDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getSender().getId(),
                message.getReceiver().getId(),
                message.getContent(),
                message.getTimestamp(),
                message.getIsRead()
        );
    }

    public List<UserDto> getConversationsByUserId(Long userId) {
        List<Long> userIds = messageRepository.findUniqueUserIdsByUserId(userId);
        List<User> users = userRepository.findAllById(userIds);
        return users.stream()
                .map(user -> new UserDto(user.getId(), user.getFirstName(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());
    }

}

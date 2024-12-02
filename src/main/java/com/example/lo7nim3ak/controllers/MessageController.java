package com.example.lo7nim3ak.controllers;

import com.example.lo7nim3ak.dto.MessageDto;
import com.example.lo7nim3ak.dto.UserDto;
import com.example.lo7nim3ak.entities.Message;
import com.example.lo7nim3ak.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<MessageDto> sendMessage(@RequestBody MessageDto messageDto) {
        Message sentMessage = messageService.sendMessage(
                messageDto.getSenderId(),
                messageDto.getReceiverId(),
                messageDto.getContent()
        );
        MessageDto responseDto = new MessageDto(
                sentMessage.getId(),
                sentMessage.getSender().getId(),
                sentMessage.getReceiver().getId(),
                sentMessage.getContent(),
                sentMessage.getTimestamp(),
                sentMessage.getIsRead()
        );
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<MessageDto>> getConversation(@RequestParam Long userId1,
                                                            @RequestParam Long userId2) {
        List<MessageDto> messages = messageService.getConversation(userId1, userId2);
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/mark-read")
    public ResponseEntity<Void> markMessagesAsRead(@RequestParam Long senderId,
                                                   @RequestParam Long receiverId) {
        messageService.markMessagesAsRead(senderId, receiverId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/my-conversations/{userId}")
    public ResponseEntity<List<UserDto>> getMyConversations(@PathVariable Long userId) {
        List<UserDto> users = messageService.getConversationsByUserId(userId);
        return ResponseEntity.ok(users);
    }

}

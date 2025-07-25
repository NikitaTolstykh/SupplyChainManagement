package com.delivery.event;

import com.delivery.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkerCreatedEvent {
    private User worker;
    private String password;
}

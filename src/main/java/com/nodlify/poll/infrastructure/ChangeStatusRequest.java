package com.nodlify.poll.infrastructure;

import com.nodlify.poll.domain.PollStatus;


record ChangeStatusRequest(String status) {

    PollStatus toStatus() {
        return PollStatus.valueOf(status.strip().toUpperCase());
    }
}

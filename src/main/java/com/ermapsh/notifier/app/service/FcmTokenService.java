package com.ermapsh.notifier.app.service;

import com.ermapsh.notifier.app.dto.req.FcmTokenRegisterReq;
import com.ermapsh.notifier.app.dto.res.FcmTokenRegisterResp;

import java.util.UUID;

public interface FcmTokenService {
    FcmTokenRegisterResp register(FcmTokenRegisterReq request);
    void unregister(UUID deviceId);
}

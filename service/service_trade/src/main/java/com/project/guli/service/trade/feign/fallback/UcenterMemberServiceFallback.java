package com.project.guli.service.trade.feign.fallback;

import com.project.guli.service.base.dto.MemberDto;
import com.project.guli.service.trade.feign.UcenterMemberService;
import org.springframework.stereotype.Service;

/**
 * @author wan
 * @create 2020-07-29-20:03
 */
@Service
public class UcenterMemberServiceFallback implements UcenterMemberService {

    @Override
    public MemberDto getMemberDtoById(String memberId) {
        return null;
    }
}

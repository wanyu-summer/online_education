package com.project.guli.service.trade.feign;

import com.project.guli.service.base.dto.MemberDto;
import com.project.guli.service.trade.feign.fallback.UcenterMemberServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author wan
 * @create 2020-07-29-19:23
 */
@Service
@FeignClient(value = "service-ucenter", fallback = UcenterMemberServiceFallback.class)
public interface UcenterMemberService {

    @GetMapping("/api/ucenter/member/inner/get-member-dto/{memberId}")
    MemberDto getMemberDtoById(@PathVariable(value = "memberId") String memberId);
}

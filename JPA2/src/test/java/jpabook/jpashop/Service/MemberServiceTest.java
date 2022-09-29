package jpabook.jpashop.Service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    MemberServiceTest() {
    }

    @Test
    public void 회원가입() throws Exception {
        Member member = new Member();
        member.setName("kim");
        Long saveId = this.memberService.join(member);
        Assertions.assertEquals(member, this.memberRepository.findOne(saveId));
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");
        this.memberService.join(member1);

        try {
            this.memberService.join(member2);
        } catch (IllegalStateException var4) {
            return;
        }

        Assertions.fail("예외가 발생해야 한다.");
    }
}

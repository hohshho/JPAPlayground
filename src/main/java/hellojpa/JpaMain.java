package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args){
        // 인자로 persistence.xml에서 설정한 DB (persistence-unit)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            // 팀 저장
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            // 회원 저장
            Member member = new Member();
            // 회원1에 새로운 팀B 설정
            member.setName("member1");

            // 양방향 매핑 시 양쪽 모두에 값 입력
            team.getMembers().add(member);
            member.setTeam(team);

            em.persist(member);

            // 영속성 컨테스트 업데이트 후 clear
            em.flush();
            em.clear();

            // 조회
            Member findMember = em.find(Member.class, member.getId());
            List<Member> members = findMember.getTeam().getMembers();

            for( Member m : members){
                System.out.println("m = " + m.getName());
            }

            // 연관관계가 없음
            Team findTeam = em.find(Team.class, team.getId());

            // 영속
            System.out.println("--- BEFORE ---");
            em.persist(member);
            System.out.println("--- AFTER ---");


            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
        }

    }
}
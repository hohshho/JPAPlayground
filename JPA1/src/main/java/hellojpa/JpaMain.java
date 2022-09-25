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

            Member member1 = new Member();
            Team team = new Team();
            team.setName("팀A");
            member1.setName("hello");
            member1.setTeam(team);
            em.persist(member1);

            // 영속성 컨테스트 업데이트 후 clear
            em.flush();
            em.clear();

//            Member findMember = em.find(Member.class, member.getId());
//            Member findMember = em.getReference(Member.class, member.getId());


//            String jpql = "select m from Member m join fetch m.team";
//            List<Member> members = em.createQuery(jpql, Member.class)
//                    .getResultList();
//
//            for (Member member : members) {
//                System.out.println("username = " + member.getName() + ", " + "teamName = " + member.getTeam().getName());
//            }


            em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();


            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
        }

    }
}
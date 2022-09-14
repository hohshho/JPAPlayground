package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args){
        // 인자로 persistence.xml에서 설정한 DB (persistence-unit)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            // 비영속
            Member member = new Member();
            member.setId(100L);
            member.setUsername("HelloJPA");

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
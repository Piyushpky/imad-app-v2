/**
 * 
 */
package com.hp.wpp.postcard.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.hp.wpp.postcard.dao.PostcardDao;
import com.hp.wpp.postcard.entities.PostcardAdditionalInfoEntity;
import com.hp.wpp.postcard.entities.PostcardCertificateInfoEntity;
import com.hp.wpp.postcard.entities.PostcardEntity;
import com.hp.wpp.postcard.entities.PostcardRenegotiationInfoEntity;

/**
 * @author mahammad
 *
 */
@Transactional("postcardTransactionManager")
public class PostcardDaoImpl implements PostcardDao {
	
	@PersistenceContext(unitName="postcard")	
	private EntityManager em;

	@Override
	public void createPostcard(PostcardEntity postcardEntity) {
		em.persist(postcardEntity);
		em.flush();
	}

	@Override
	public void updatePostcard(PostcardEntity postcardEntity) {
		em.merge(postcardEntity);
	}

	@Override
	public PostcardEntity getPostcard(String entityId) {
		Query query = em.createNamedQuery("getPostcard").setParameter("entityId", entityId);
		try {
			return (PostcardEntity) query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public void deletePostcard(PostcardEntity postcardEntity) {
		postcardEntity = em.merge(postcardEntity);
		em.remove(postcardEntity);
	}

	@Override
	public PostcardAdditionalInfoEntity getPostcardAdditionalInfo(PostcardEntity postcardEntity, String applicationId) {
		Query query = em.createNamedQuery("getPostcardAdditionalInfo").setParameter("applicationId", applicationId).setParameter("postcardEntity", postcardEntity);
		try {
			return (PostcardAdditionalInfoEntity) query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public void updatePostcardAdditionalInfo(PostcardAdditionalInfoEntity postcardAdditionalInfoEntity) {
		em.merge(postcardAdditionalInfoEntity);
	}

	@Override
	public void store(PostcardRenegotiationInfoEntity postcardRenegotiationInfoEntity) {
		em.persist(postcardRenegotiationInfoEntity);
	}

	@Override
	public List<PostcardRenegotiationInfoEntity> getPostcardRenegotiationInfos(PostcardEntity postcardEntity, String applicationId) {
		Query query = em.createNamedQuery("getPostcardRenegotiationInfo").setParameter("applicationId", applicationId).setParameter("postcardEntity", postcardEntity);
		try {
			return (List<PostcardRenegotiationInfoEntity>) query.getResultList();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public PostcardCertificateInfoEntity getPostcardCertificateInfo(String publicKeySerialNumber) {
		Query query = em.createNamedQuery("getPostcardCertificateInfo").setParameter("certificateSerialNum", publicKeySerialNumber);
		try {
			return (PostcardCertificateInfoEntity) query.getResultList();
		} catch (NoResultException ex) {
			return null;
		}
	}

}

package net.gplatform.sudoor.server.ss;

import net.gplatform.sudoor.server.ss.entity.CredentialUser;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 13-12-25.
 */
public interface CredentialUserRepository extends JpaRepository<CredentialUser, String> {
}

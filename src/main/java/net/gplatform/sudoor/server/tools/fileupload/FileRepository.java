package net.gplatform.sudoor.server.tools.fileupload;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-18
 * Time: 下午9:34
 * To change this template use File | Settings | File Templates.
 */
public interface FileRepository extends JpaRepository<File,Long> {
}

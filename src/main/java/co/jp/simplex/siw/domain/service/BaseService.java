package co.jp.simplex.siw.domain.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviceクラスの基底クラス、共通処理をここに実装する。
 * 
 * @author nakanoya
 *
 */
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
abstract public class BaseService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    protected ModelMapper modelMapper = new ModelMapper();
}

package yuown.yenkay.familytree.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheHelper {

    @CacheEvict(cacheNames = "members-id")
    public void clearMembersCache() {
        log.debug("Cleared members-id cache");
    }

}

package graphEndpoint.dataConnection.versioning.service;

import graphEndpoint.dataConnection.domain.DomainEntity;
import graphEndpoint.dataConnection.versioning.domain.VersionPathHead;
import graphEndpoint.dataConnection.versioning.domain.VersionWrapper;
import graphEndpoint.dataConnection.versioning.repository.CurrentVersionRepository;
import graphEndpoint.dataConnection.versioning.repository.VersionRepository;
import graphEndpoint.dataConnection.versioning.repository.VersionedNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Daniel Hons on 29.04.2017.
 */
@Service
public class VersioningService<T extends DomainEntity> {
    @Autowired
    private VersionedNodeRepository<T> versionedNodeRepository;
    @Autowired
    private VersionRepository<T> versionRepository;
    @Autowired
    private CurrentVersionRepository<T> currentVersionRepository;



    public VersionPathHead<T> create(T newNode)
    {
        VersionPathHead<T> current=new VersionPathHead<>();
        current.setNode(newNode);
        versionedNodeRepository.save(newNode);
        currentVersionRepository.save(current);
        return  current;
    }

    public VersionPathHead<T> getCurrentVersion(String uuid)
    {
        return currentVersionRepository.findByHistoryUuid(uuid,2);


    }

    public VersionPathHead<T> pushNewVersion(VersionPathHead<T> currentVersion, T newVersionNode)
    {
        newVersionNode=versionedNodeRepository.save(newVersionNode,0);
        VersionWrapper<T> oldVersionWrapper =currentVersion.createPrevious();

        oldVersionWrapper =versionRepository.save(oldVersionWrapper,1);
        currentVersion.setNode(newVersionNode);
        currentVersion.setPreviousVersionWrapper(oldVersionWrapper);
        currentVersion=currentVersionRepository.save(currentVersion,1);

        return currentVersion;
    }

    public VersionPathHead<T> removeBind(String uuid){
        VersionPathHead<T> current=this.getCurrentVersion(uuid);
        current.setPreviousVersionWrapper(null);
        return currentVersionRepository.save(current);
    }


}

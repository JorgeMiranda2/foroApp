package com.foro.foro.Service;

import com.foro.foro.Model.Profile;
import com.foro.foro.Repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional
    public Profile saveProfile(Profile profile){
        return profileRepository.save(profile);
    }

    public Optional<Profile> findProfileById(Long id){
        return profileRepository.findById(id);
    }

    public List<String> getAllProfileNames() {
        return profileRepository.findAll().stream().map(Profile::getName).collect(Collectors.toList());
    }
}

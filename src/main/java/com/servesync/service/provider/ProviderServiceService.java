package com.servesync.service.provider;

import com.servesync.dto.provider.ProviderServiceCreateDTO;
import com.servesync.dto.provider.ProviderServiceDTO;
import com.servesync.dto.provider.ProviderServiceUpdateDTO;

import java.util.List;

public interface ProviderServiceService {

    /**
     * Retrieve all ProviderService records.
     * @return list of ProviderServiceDto
     */
    List<ProviderServiceDTO> getAll();

    /**
     * Retrieve a ProviderService by its ID.
     * @param id ProviderService id
     * @return ProviderServiceDto
     */
    ProviderServiceDTO getById(Long id);

    /**
     * Create a new ProviderService.
     * @param createDto data to create ProviderService
     * @return created ProviderServiceDto
     */
    ProviderServiceDTO create(ProviderServiceCreateDTO createDto);

    /**
     * Update an existing ProviderService.
     * @param id id of ProviderService to update
     * @param updateDto update data
     * @return updated ProviderServiceDto
     */
    ProviderServiceDTO update(Long id, ProviderServiceUpdateDTO updateDto);

    /**
     * Delete a ProviderService by id.
     * @param id id of ProviderService to delete
     */
    void delete(Long id);
}

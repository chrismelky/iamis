package com.deeptech.iamis.modules.gfs_code;

import com.deeptech.iamis.core.SearchService;
import com.deeptech.iamis.core.Utils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing GfsCodes. Provides CRUD operations for GfsCodes
 * This service uses {@link GfsCodeRepository} for database operations and {@link GfsCodeMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for GfsCodes.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GfsCodeServiceImpl
  extends SearchService<GfsCode>
  implements GfsCodeService {

  private final GfsCodeRepository gfsCodeRepository;

  private final GfsCodeMapper gfsCodeMapper;

  /**
   * Creates or updates a GfsCode based on the presence of a UUID in the DTO.
   *
   * @param gfsCodeDto the {@link GfsCodeDto} containing GfsCode data.
   * @return the saved {@link GfsCodeDto}.
   * @throws EntityNotFoundException if the GfsCode is not found for update or there is validation failure.
   */
  @Override
  public GfsCodeDto save(GfsCodeDto gfsCodeDto) {
    // Convert the DTO to an entity object
    GfsCode gfsCode = gfsCodeMapper.toEntity(gfsCodeDto);

    // If the GfsCode has a UUID, fetch the existing GfsCode and perform an update
    if (gfsCodeDto.getUuid() != null) {
      gfsCode =
        gfsCodeRepository
          .findByUuid(gfsCodeDto.getUuid())
          .orElseThrow(() ->
            new EntityNotFoundException(
              "gfsCode with uuid" + gfsCodeDto.getUuid() + " not found"
            )
          );
      // Partially update the existing GfsCode with new data from the DTO
      gfsCode = gfsCodeMapper.partialUpdate(gfsCodeDto, gfsCode);
    } else {
      // Set a new UUID for a new GfsCode
      gfsCode.setUuid(Utils.generateUuid());
    }
    // Save the GfsCode and return the DTO
    gfsCode = gfsCodeRepository.save(gfsCode);
    return gfsCodeMapper.toDto(gfsCode);
  }

  /**
   * Finds all GfsCodes based on pagination and search criteria.
   *
   * @param pageable the pagination information.
   * @param searchParams   a map of search criteria.
   * @return a paginated list of {@link GfsCodeDto}.
   */
  @Override
  public Page<GfsCodeDto> findAll(
    Pageable pageable,
    Map<String, Object> searchParams
  ) {
    return gfsCodeRepository
      .findAll(createSpecification(GfsCode.class, searchParams), pageable)
      .map(gfsCodeMapper::toDto);
  }

  /**
   * Finds a GfsCode by its UUID.
   *
   * @param uuid the UUID of the GfsCode to find.
   * @return the {@link GfsCodeDto} if found.
   * @throws ValidationException if the GfsCode is not found.
   */
  @Override
  public GfsCodeDto findById(UUID uuid) {
    return gfsCodeRepository
      .findByUuid(uuid)
      .map(gfsCodeMapper::toDto)
      .orElseThrow(() ->
        new EntityNotFoundException("GfsCode  with uuid " + uuid + " not found")
      );
  }

  /**
   * Deletes a GfsCode by its UUID.
   *
   * @param uuid the UUID of the GfsCode to delete.
   * @throws ValidationException if the GfsCode cannot be deleted.
   */
  @Override
  public void delete(UUID uuid) {
    try {
      gfsCodeRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      throw new ValidationException("Cannot delete GfsCode with uuid " + uuid);
    }
  }
}

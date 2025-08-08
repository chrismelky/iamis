package com.deeptech.iamis.modules.finding_subcategory;

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
 * Service implementation for managing FindingSubcategories. Provides CRUD operations for FindingSubcategories
 * This service uses {@link FindingSubcategoryRepository} for database operations and {@link FindingSubcategoryMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for FindingSubcategories.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FindingSubcategoryServiceImpl
  extends SearchService<FindingSubcategory>
  implements FindingSubcategoryService {

  private final FindingSubcategoryRepository findingSubcategoryRepository;

  private final FindingSubcategoryMapper findingSubcategoryMapper;

  /**
   * Creates or updates a FindingSubcategory based on the presence of a UUID in the DTO.
   *
   * @param findingSubcategoryDto the {@link FindingSubcategoryDto} containing FindingSubcategory data.
   * @return the saved {@link FindingSubcategoryDto}.
   * @throws EntityNotFoundException if the FindingSubcategory is not found for update or there is validation failure.
   */
  @Override
  public FindingSubcategoryDto save(
    FindingSubcategoryDto findingSubcategoryDto
  ) {
    // Convert the DTO to an entity object
    FindingSubcategory findingSubcategory = findingSubcategoryMapper.toEntity(
      findingSubcategoryDto
    );

    // If the FindingSubcategory has a UUID, fetch the existing FindingSubcategory and perform an update
    if (findingSubcategoryDto.getUuid() != null) {
      findingSubcategory =
        findingSubcategoryRepository
          .findByUuid(findingSubcategoryDto.getUuid())
          .orElseThrow(() ->
            new EntityNotFoundException(
              "findingSubcategory with uuid" +
              findingSubcategoryDto.getUuid() +
              " not found"
            )
          );
      // Partially update the existing FindingSubcategory with new data from the DTO
      findingSubcategory =
        findingSubcategoryMapper.partialUpdate(
          findingSubcategoryDto,
          findingSubcategory
        );
    } else {
      // Set a new UUID for a new FindingSubcategory
      findingSubcategory.setUuid(Utils.generateUuid());
    }
    // Save the FindingSubcategory and return the DTO
    findingSubcategory = findingSubcategoryRepository.save(findingSubcategory);
    return findingSubcategoryMapper.toDto(findingSubcategory);
  }

  /**
   * Finds all FindingSubcategories based on pagination and search criteria.
   *
   * @param pageable the pagination information.
   * @param searchParams   a map of search criteria.
   * @return a paginated list of {@link FindingSubcategoryDto}.
   */
  @Override
  public Page<FindingSubcategoryDto> findAll(
    Pageable pageable,
    Map<String, Object> searchParams
  ) {
    return findingSubcategoryRepository
      .findAll(
        createSpecification(FindingSubcategory.class, searchParams),
        pageable
      )
      .map(findingSubcategoryMapper::toDto);
  }

  /**
   * Finds a FindingSubcategory by its UUID.
   *
   * @param uuid the UUID of the FindingSubcategory to find.
   * @return the {@link FindingSubcategoryDto} if found.
   * @throws ValidationException if the FindingSubcategory is not found.
   */
  @Override
  public FindingSubcategoryDto findById(UUID uuid) {
    return findingSubcategoryRepository
      .findByUuid(uuid)
      .map(findingSubcategoryMapper::toDto)
      .orElseThrow(() ->
        new EntityNotFoundException(
          "FindingSubcategory  with uuid " + uuid + " not found"
        )
      );
  }

  /**
   * Deletes a FindingSubcategory by its UUID.
   *
   * @param uuid the UUID of the FindingSubcategory to delete.
   * @throws ValidationException if the FindingSubcategory cannot be deleted.
   */
  @Override
  public void delete(UUID uuid) {
    try {
      findingSubcategoryRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete FindingSubcategory with uuid " + uuid
      );
    }
  }
}

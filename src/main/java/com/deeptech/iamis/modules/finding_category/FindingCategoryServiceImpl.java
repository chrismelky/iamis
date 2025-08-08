package com.deeptech.iamis.modules.finding_category;

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
 * Service implementation for managing FindingCategories. Provides CRUD operations for FindingCategories
 * This service uses {@link FindingCategoryRepository} for database operations and {@link FindingCategoryMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for FindingCategories.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FindingCategoryServiceImpl
  extends SearchService<FindingCategory>
  implements FindingCategoryService {

  private final FindingCategoryRepository findingCategoryRepository;

  private final FindingCategoryMapper findingCategoryMapper;

  /**
   * Creates or updates a FindingCategory based on the presence of a UUID in the DTO.
   *
   * @param findingCategoryDto the {@link FindingCategoryDto} containing FindingCategory data.
   * @return the saved {@link FindingCategoryDto}.
   * @throws EntityNotFoundException if the FindingCategory is not found for update or there is validation failure.
   */
  @Override
  public FindingCategoryDto save(FindingCategoryDto findingCategoryDto) {
    // Convert the DTO to an entity object
    FindingCategory findingCategory = findingCategoryMapper.toEntity(
      findingCategoryDto
    );

    // If the FindingCategory has a UUID, fetch the existing FindingCategory and perform an update
    if (findingCategoryDto.getUuid() != null) {
      findingCategory =
        findingCategoryRepository
          .findByUuid(findingCategoryDto.getUuid())
          .orElseThrow(() ->
            new EntityNotFoundException(
              "findingCategory with uuid" +
              findingCategoryDto.getUuid() +
              " not found"
            )
          );
      // Partially update the existing FindingCategory with new data from the DTO
      findingCategory =
        findingCategoryMapper.partialUpdate(
          findingCategoryDto,
          findingCategory
        );
    } else {
      // Set a new UUID for a new FindingCategory
      findingCategory.setUuid(Utils.generateUuid());
    }
    // Save the FindingCategory and return the DTO
    findingCategory = findingCategoryRepository.save(findingCategory);
    return findingCategoryMapper.toDto(findingCategory);
  }

  /**
   * Finds all FindingCategories based on pagination and search criteria.
   *
   * @param pageable the pagination information.
   * @param searchParams   a map of search criteria.
   * @return a paginated list of {@link FindingCategoryDto}.
   */
  @Override
  public Page<FindingCategoryDto> findAll(
    Pageable pageable,
    Map<String, Object> searchParams
  ) {
    return findingCategoryRepository
      .findAll(
        createSpecification(FindingCategory.class, searchParams),
        pageable
      )
      .map(findingCategoryMapper::toDto);
  }

  /**
   * Finds a FindingCategory by its UUID.
   *
   * @param uuid the UUID of the FindingCategory to find.
   * @return the {@link FindingCategoryDto} if found.
   * @throws ValidationException if the FindingCategory is not found.
   */
  @Override
  public FindingCategoryDto findById(UUID uuid) {
    return findingCategoryRepository
      .findByUuid(uuid)
      .map(findingCategoryMapper::toDto)
      .orElseThrow(() ->
        new EntityNotFoundException(
          "FindingCategory  with uuid " + uuid + " not found"
        )
      );
  }

  /**
   * Deletes a FindingCategory by its UUID.
   *
   * @param uuid the UUID of the FindingCategory to delete.
   * @throws ValidationException if the FindingCategory cannot be deleted.
   */
  @Override
  public void delete(UUID uuid) {
    try {
      findingCategoryRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete FindingCategory with uuid " + uuid
      );
    }
  }
}

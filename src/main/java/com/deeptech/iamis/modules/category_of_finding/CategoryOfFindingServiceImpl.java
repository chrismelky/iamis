package com.deeptech.iamis.modules.category_of_finding;

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
 * Service implementation for managing CategoryOfFindings. Provides CRUD operations for CategoryOfFindings
 * This service uses {@link CategoryOfFindingRepository} for database operations and {@link CategoryOfFindingMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for CategoryOfFindings.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryOfFindingServiceImpl
  extends SearchService<CategoryOfFinding>
  implements CategoryOfFindingService {

  private final CategoryOfFindingRepository categoryOfFindingRepository;

  private final CategoryOfFindingMapper categoryOfFindingMapper;

  /**
   * Creates or updates a CategoryOfFinding based on the presence of a UUID in the DTO.
   *
   * @param categoryOfFindingDto the {@link CategoryOfFindingDto} containing CategoryOfFinding data.
   * @return the saved {@link CategoryOfFindingDto}.
   * @throws EntityNotFoundException if the CategoryOfFinding is not found for update or there is validation failure.
   */
  @Override
  public CategoryOfFindingDto save(CategoryOfFindingDto categoryOfFindingDto) {
    // Convert the DTO to an entity object
    CategoryOfFinding categoryOfFinding = categoryOfFindingMapper.toEntity(
      categoryOfFindingDto
    );

    // If the CategoryOfFinding has a UUID, fetch the existing CategoryOfFinding and perform an update
    if (categoryOfFindingDto.getUuid() != null) {
      categoryOfFinding =
        categoryOfFindingRepository
          .findByUuid(categoryOfFindingDto.getUuid())
          .orElseThrow(() ->
            new EntityNotFoundException(
              "categoryOfFinding with uuid" +
              categoryOfFindingDto.getUuid() +
              " not found"
            )
          );
      // Partially update the existing CategoryOfFinding with new data from the DTO
      categoryOfFinding =
        categoryOfFindingMapper.partialUpdate(
          categoryOfFindingDto,
          categoryOfFinding
        );
    } else {
      // Set a new UUID for a new CategoryOfFinding
      categoryOfFinding.setUuid(Utils.generateUuid());
    }
    // Save the CategoryOfFinding and return the DTO
    categoryOfFinding = categoryOfFindingRepository.save(categoryOfFinding);
    return categoryOfFindingMapper.toDto(categoryOfFinding);
  }

  /**
   * Finds all CategoryOfFindings based on pagination and search criteria.
   *
   * @param pageable the pagination information.
   * @param searchParams   a map of search criteria.
   * @return a paginated list of {@link CategoryOfFindingDto}.
   */
  @Override
  public Page<CategoryOfFindingDto> findAll(
    Pageable pageable,
    Map<String, Object> searchParams
  ) {
    return categoryOfFindingRepository
      .findAll(
        createSpecification(CategoryOfFinding.class, searchParams),
        pageable
      )
      .map(categoryOfFindingMapper::toDto);
  }

  /**
   * Finds a CategoryOfFinding by its UUID.
   *
   * @param uuid the UUID of the CategoryOfFinding to find.
   * @return the {@link CategoryOfFindingDto} if found.
   * @throws ValidationException if the CategoryOfFinding is not found.
   */
  @Override
  public CategoryOfFindingDto findById(UUID uuid) {
    return categoryOfFindingRepository
      .findByUuid(uuid)
      .map(categoryOfFindingMapper::toDto)
      .orElseThrow(() ->
        new EntityNotFoundException(
          "CategoryOfFinding  with uuid " + uuid + " not found"
        )
      );
  }

  /**
   * Deletes a CategoryOfFinding by its UUID.
   *
   * @param uuid the UUID of the CategoryOfFinding to delete.
   * @throws ValidationException if the CategoryOfFinding cannot be deleted.
   */
  @Override
  public void delete(UUID uuid) {
    try {
      categoryOfFindingRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete CategoryOfFinding with uuid " + uuid
      );
    }
  }
}

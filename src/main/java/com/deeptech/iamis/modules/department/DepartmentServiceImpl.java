package com.deeptech.iamis.modules.department;

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
 * Service implementation for managing Departments. Provides CRUD operations for Departments
 * This service uses {@link DepartmentRepository} for database operations and {@link DepartmentMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for Departments.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentServiceImpl
  extends SearchService<Department>
  implements DepartmentService {

  private final DepartmentRepository departmentRepository;

  private final DepartmentMapper departmentMapper;

  /**
   * Creates or updates a Department based on the presence of a UUID in the DTO.
   *
   * @param departmentDto the {@link DepartmentDto} containing Department data.
   * @return the saved {@link DepartmentDto}.
   * @throws EntityNotFoundException if the Department is not found for update or there is validation failure.
   */
  @Override
  public DepartmentDto save(DepartmentDto departmentDto) {
    // Convert the DTO to an entity object
    Department department = departmentMapper.toEntity(departmentDto);

    // If the Department has a UUID, fetch the existing Department and perform an update
    if (departmentDto.getUuid() != null) {
      department =
        departmentRepository
          .findByUuid(departmentDto.getUuid())
          .orElseThrow(() ->
            new EntityNotFoundException(
              "department with uuid" + departmentDto.getUuid() + " not found"
            )
          );
      // Partially update the existing Department with new data from the DTO
      department = departmentMapper.partialUpdate(departmentDto, department);
    } else {
      // Set a new UUID for a new Department
      department.setUuid(Utils.generateUuid());
    }
    // Save the Department and return the DTO
    department = departmentRepository.save(department);
    return departmentMapper.toDto(department);
  }

  /**
   * Finds all Departments based on pagination and search criteria.
   *
   * @param pageable the pagination information.
   * @param searchParams   a map of search criteria.
   * @return a paginated list of {@link DepartmentDto}.
   */
  @Override
  public Page<DepartmentDto> findAll(
    Pageable pageable,
    Map<String, Object> searchParams
  ) {
    return departmentRepository
      .findAll(createSpecification(Department.class, searchParams), pageable)
      .map(departmentMapper::toDto);
  }

  /**
   * Finds a Department by its UUID.
   *
   * @param uuid the UUID of the Department to find.
   * @return the {@link DepartmentDto} if found.
   * @throws ValidationException if the Department is not found.
   */
  @Override
  public DepartmentDto findById(UUID uuid) {
    return departmentRepository
      .findByUuid(uuid)
      .map(departmentMapper::toDto)
      .orElseThrow(() ->
        new EntityNotFoundException(
          "Department  with uuid " + uuid + " not found"
        )
      );
  }

  /**
   * Deletes a Department by its UUID.
   *
   * @param uuid the UUID of the Department to delete.
   * @throws ValidationException if the Department cannot be deleted.
   */
  @Override
  public void delete(UUID uuid) {
    try {
      departmentRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete Department with uuid " + uuid
      );
    }
  }
}

package com.company.department;

import com.company.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    public Department getById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    public Department create(Department department) {
        if (departmentRepository.existsByNameIgnoreCase(department.getName())) {
            throw new IllegalArgumentException("Department name already exists");
        }
        department.setId(null);
        return departmentRepository.save(department);
    }

    public Department update(Long id, Department request) {
        Department department = getById(id);
        if (!department.getName().equalsIgnoreCase(request.getName())
                && departmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Department name already exists");
        }
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        return departmentRepository.save(department);
    }

    public void delete(Long id) {
        Department department = getById(id);
        departmentRepository.delete(department);
    }
}

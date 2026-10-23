package com.company.employee;

import com.company.department.Department;
import com.company.department.DepartmentRepository;
import com.company.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Employee getById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    public Employee create(Employee employee) {
        if (employeeRepository.existsByEmailIgnoreCase(employee.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Department department = findDepartment(employee.getDepartment().getId());
        employee.setId(null);
        employee.setDepartment(department);
        return employeeRepository.save(employee);
    }

    public Employee update(Long id, Employee request) {
        Employee employee = getById(id);
        if (!employee.getEmail().equalsIgnoreCase(request.getEmail())
                && employeeRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setSalary(request.getSalary());
        employee.setDepartment(findDepartment(request.getDepartment().getId()));
        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        employeeRepository.delete(getById(id));
    }

    private Department findDepartment(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Department id is required");
        }
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }
}

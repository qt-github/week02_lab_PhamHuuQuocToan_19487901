# Week-02-Lab

Đây là một dự án Java sử dụng API Jakarta Persistence để tương tác với cơ sở dữ liệu MariaDB.

## Prerequisites

- Java 8 or higher
- Gradle
- MariaDB

## Setup

1. Clone the repository.
2. Cập nhật tệp `src/main/resources/META-INF/persistence.xml` với chi tiết kết nối MariaDB của bạn.
3. Chạy dự án bằng Gradle.

## Features

Dự án bao gồm các tính năng sau:

- Entities: `Product`, `Employee`, `Order`, `Customer`, `ProductImage`, `ProductPrice`, `OrderDetail`.
- Converters: `ProductStatus` and `EmployeeStatus` enums.
- A persistence unit named `lab_week_2`.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.


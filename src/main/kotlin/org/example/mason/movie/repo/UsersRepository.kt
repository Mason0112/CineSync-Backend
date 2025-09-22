package org.example.mason.movie.repo

import org.example.mason.movie.model.entity.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UsersRepository : JpaRepository<Users, Long>, JpaSpecificationExecutor<Users>{

    fun findByEmail(email: String): Optional<Users>
}
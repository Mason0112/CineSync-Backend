package org.example.mason.movie.repo

import org.example.mason.movie.model.entity.Comments
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable



@Repository
interface CommentsRepository : JpaRepository<Comments, Long>, JpaSpecificationExecutor<Comments>{

    fun findByUserId(userId: Long, pageable: Pageable): Page<Comments>
    fun countByUserId(userId: Long): Long


}

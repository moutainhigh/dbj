﻿using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace dbdesgin.Models
{
    [Table("core_roles")]
    public class Role
    {
        [Required]
        [MaxLength(50)]
        [Key]
        public string name { get; set; }
        [MaxLength(512)]
        public string description { get; set; } 
    }
}

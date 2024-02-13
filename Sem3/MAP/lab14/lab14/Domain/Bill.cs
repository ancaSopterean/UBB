using System;
using System.Collections.Generic;
using System.Security.Authentication.ExtendedProtection.Configuration;
using lab14.Domain.DTO;

namespace lab14.Domain
{
    public enum Category
    {
        Utilities, Groceries, Fashion, Entertainment, Electronics
    }

    public class Bill : Document
    {
        public DateTime DueDate { get; set; }
        public List<Acquisition> Acquisitions { get; set; }
        public Category Category { get; set; }

        public Bill(string id, string name, DateTime issueDate, DateTime dueDate, List<Acquisition> acqisitions,
            Category category)
        {
            Id = id;
            Name = name;
            IssueDate = issueDate;
            DueDate = dueDate;
            Acquisitions = acqisitions;
            Category = category;
        }
        public Bill(){}
        public override string ToString()
        {
            return $"Id: {Id} | Name: {{Name}} | IssueDate: {{IssueDate}} | DueDate: {{DueDate}} | \" +\n        " +
                              $"Acquisitions: {Acquisitions} | Category: {Category}";
        }
    }
}
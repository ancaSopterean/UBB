using System;

namespace lab14.Domain
{
    public class Document : Entity<string>
    {
        public string Name { get; set; }
        public DateTime IssueDate { get; set; }

        public Document(string id, string name, DateTime issueDate)
        {
            Id = id;
            Name = name;
            IssueDate = issueDate;
        }
        protected Document(){}
        public override string ToString()
        {
            return $"Name: {Name} | IssueDate: {IssueDate}";
        }
    }
}
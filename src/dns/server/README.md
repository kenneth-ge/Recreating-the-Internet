# Request/response format
## Register Domain
### Request
The request is compressed all the way down to byte level. The format is as follows:
| Byte # | 0 | 1 through 4 | 5-6         | 7+          |
|--------|---|-------------|-------------|-------------|
| Data   | 0 | IP Address  | Port number | Domain name |

### Response
The response is simply a 0, indicating that the task completed successfully, or a number, representing the error code. 
### Error Codes
| Code | Explanation |
|--------|-----------|
| 1   | Unknown error occurred |

## Resolve name
### Request
The request is compressed all the way down to byte level. The format is as follows:

| Byte # | 0 | 1+ |
|--------|---|-------------|
| Data   | 1 | Domain name |

### Response
The response is compressed all the way down to byte level. The format is as follows:

| Byte # | 0 | 1 through 4 | 5 and 6     |
|--------|---|------------|-------------|
| Data   | Status code | IP Address  | Port number |

### Status Codes
| Code | Explanation |
|--------|-----------|
| 0   | Successful |
| 1   | Unknown error occurred |
| 2   | Not found |

# File format (database format)
File is saved in data/domains.data
Format:
	Each line has its own domain record. Each domain record is as follows:
	IP;Port;Domain name
	E.g.
	12398123;8000;local text server
	Note that the IP address is given in the format of a long, rather than a readable IP. 
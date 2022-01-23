# Overall methodology
This server uses a tree to store domain records. In essence, it tries to find an exact match for your domain name, then goes up in levels until it finds a match. E.g. if you have the following records

| Name | IP Address | Port number | Type |
|------|------------|-------------|------|
| com.test | 127.0.0.1 | 5000 | NS |
| com.test.hi | 127.0.0.1 | 6000 | A |

And you query com.test.hi, it will return 127.0.0.1:6000. This may cause issues if, like the above, you've defined an NS record for a domain and then also included A records for subdomains. 

One important thing to note (unrelated to the above) is that you can only store one record for each domain name. E.g. you can't have an NS record and an A record attached to the same domain name. 

# Request/response format
## Register Domain
### Request
The request is compressed all the way down to byte level. The format is as follows:
| Byte # | 0 | 1 | 2 through 5 | 6-7         | 8+          |
|--------|---|---|-------------|-------------|-------------|
| Data   | 0 | Type of record | IP Address  | Port number | Domain name |

### Types of Records
| Code | Explanation |
|--------|-----------|
| 0   | A record, e.g. maps to a program running on a specific IP address and port number |
| 1   | NS record, e.g. maps to another DNS server for the selected subdomain |

### Response
The response is simply a 0, indicating that the task completed successfully, or a number, representing the error code. 

### Error Codes
| Code | Explanation |
|--------|-----------|
| 1   | Unknown error occurred |
| 2   | Domain record already exists |

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
| Data   | Status code/response type | IP Address  | Port number |

### Status Codes/response types
| Code | Explanation |
|--------|-----------|
| 0   | Successful -- type is A record  |
| 1   | Successful -- type is NS record |
| 254 | Not found |
| 255 | Unknown error occurred |

# File format (database format)
File is saved in data/domains.data
Format:
	Each line has its own domain record. Each domain record is as follows:
	IP;Port;Domain name
	E.g.
	12398123;8000;local text server
	Note that the IP address is given in the format of a long, rather than a readable IP. 
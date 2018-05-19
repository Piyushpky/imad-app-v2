# avdls_dynamodb-cookbook

This cookbook installs the local version of [DynamoDB](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Tools.DynamoDBLocal.html)

## Attributes

<table>
  <tr>
    <th>Key</th>
    <th>Type</th>
    <th>Description</th>
    <th>Default</th>
  </tr>
  <tr>
    <td><tt>['avdls_dynamodb']['name']</tt></td>
    <td>String</td>
    <td></td>
    <td><tt>avdls_dynamodb</tt></td>
  </tr>
  <tr>
    <td><tt>['avdls_dynamodb']['user']</tt></td>
    <td>String</td>
    <td>System user name</td>
    <td><tt>dynamodb</tt></td>
  </tr>
  <tr>
    <td><tt>['avdls_dynamodb']['directory']</tt></td>
    <td>String</td>
    <td>Directory to install into</td>
    <td><tt>/usr/local/lib/avdls_dynamodb</tt></td>
  </tr>
  <tr>
    <td><tt>['avdls_dynamodb']['log_dir']</tt></td>
    <td>String</td>
    <td></td>
    <td><tt>/var/log/avdls_dynamodb</tt></td>
  </tr>
  <tr>
    <td><tt>['avdls_dynamodb']['download_url']</tt></td>
    <td>String</td>
    <td>http://avdls_dynamodb.s3-website-us-west-2.amazonaws.com/dynamodb_local_latest</td>
    <td><tt>avdls_dynamodb</tt></td>
  </tr>
  <tr>
    <td><tt>['avdls_dynamodb']['port']</tt></td>
    <td>Integer</td>
    <td>Port to run on</td>
    <td><tt>8000</tt></td>
  </tr>
</table>

## Usage

### avdls_dynamodb::default

Include `avdls_dynamodb` in your node's `run_list`:

```json
{
  "run_list": [
    "recipe[avdls_dynamodb::default]"
  ]
}
```

### avdls_dynamodb::upstart

Include `avdls_dynamodb` in your node's `run_list`:

```json
{
  "run_list": [
    "recipe[avdls_dynamodb::upstart]"
  ]
}
```

## License and Authors

Author:: Timehop (<tech@timehop.com>)

MIT License - see `LICENSE`

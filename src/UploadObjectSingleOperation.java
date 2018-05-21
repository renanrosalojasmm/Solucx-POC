
import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class UploadObjectSingleOperation {

    private static final String BUCKET_NAME = "solucxlojasmm";
    private static final String KEY_NAME = "output";
    private static final String UPLOAD_FILE_NAME = "output.csv";
    private static final BasicAWSCredentials AWS_CREDENTIALS = new BasicAWSCredentials("AKIAJ72DLKXBK7CU7G7Q", "z1E8oLB92QVvGBEscjqWAVYeD9UJZ4Zmo3l0X6p5");

    public boolean upload() {
        AmazonS3 s3client = new AmazonS3Client(AWS_CREDENTIALS);
        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            File file = new File(UPLOAD_FILE_NAME);
            s3client.putObject(new PutObjectRequest(
                    BUCKET_NAME, KEY_NAME, file));
            return true;

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which "
                    + "means your request made it "
                    + "to Amazon S3, but was rejected with an error response"
                    + " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which "
                    + "means the client encountered "
                    + "an internal error while trying to "
                    + "communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
        return false;
    }
}

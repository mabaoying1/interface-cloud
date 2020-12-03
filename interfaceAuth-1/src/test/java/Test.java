import com.bsoft.HttpUtil;

public class Test {
    public static void main(String[] args) {
        String jsonParam = "{\"hospitalId\":\"123\",\"pageNum\":\"1\",\"pageSize\":\"10\"}";
        HttpUtil.getHttpResult("http://localhost:9000/", "register", "listDepartment", jsonParam);
    }
}

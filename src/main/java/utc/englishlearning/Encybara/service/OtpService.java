package utc.englishlearning.Encybara.service;

import org.springframework.stereotype.Service;
import utc.englishlearning.Encybara.domain.dto.RegisterDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



@Service
public class OtpService {
    private  final  Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final  Map<String, RegisterDTO> pendingRegistration = new ConcurrentHashMap<>();
    private final  Map<String, Long> otpTimestamps = new ConcurrentHashMap<>();
    private  static final long OTP_EXPIRATION_TIME = 5 * 60 * 1000; // 5 phút


    public String generateOtp(String email){
        String otp= String.valueOf((int) (Math.random()*900000+100000));
        otpStorage.put(email, otp);
        otpTimestamps.put(email, System.currentTimeMillis());
        return otp;
    }

    //Xác thực OTP
    public boolean validateOtp(String email, String otp) {
        // Kiểm tra xem OTP có tồn tại trong bộ nhớ hay không
        String storedOtp = otpStorage.get(email);
        if (storedOtp == null) {
            return false;
        }
        Long timestamp = otpTimestamps.get(email);
        if (timestamp == null) {
            return false;
        }
        if (storedOtp.equals(otp)) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - timestamp <= OTP_EXPIRATION_TIME) {
                otpStorage.remove(email);
                otpTimestamps.remove(email);
                return true;
            }
        }
        return false;
    }
    public void saveRegisterData(String email, RegisterDTO registerDTO){
        pendingRegistration.put(email, registerDTO);
    }
    public RegisterDTO getRegisterData(String email){
        return pendingRegistration.remove(email);
    }

}

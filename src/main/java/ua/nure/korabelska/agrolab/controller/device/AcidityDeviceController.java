package ua.nure.korabelska.agrolab.controller.device;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.korabelska.agrolab.dto.device.DeviceAcidityDto;
import ua.nure.korabelska.agrolab.exception.UserNotFoundException;
import ua.nure.korabelska.agrolab.model.User;
import ua.nure.korabelska.agrolab.model.device.AcidityDevice;
import ua.nure.korabelska.agrolab.security.jwt.JwtTokenProvider;
import ua.nure.korabelska.agrolab.service.AcidityDeviceService;
import ua.nure.korabelska.agrolab.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/devices/acidity")
public class AcidityDeviceController {

  private final JwtTokenProvider jwtTokenProvider;

  private final UserService userService;

  private final AcidityDeviceService deviceService;

  @GetMapping("/{id}")
  public ResponseEntity<?> getDeviceById(@PathVariable Long id, HttpServletRequest request) {
    String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
    User user;
    try {
      user = userService.findByUsername(username);
    } catch (UserNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    AcidityDevice device = deviceService.getDevice(id);
    if(device== null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    if(!device.getActive()) {
      return ResponseEntity.ok("This device has been disabled by admin.");
    }
    return ResponseEntity.ok(device);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateDevice(@PathVariable Long id, @RequestBody DeviceAcidityDto deviceAcidityDto,
      HttpServletRequest request) {
    String username = jwtTokenProvider.getUserName(jwtTokenProvider.resolveToken(request));
    User user;
    try {
      user = userService.findByUsername(username);
    } catch (UserNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    AcidityDevice device = deviceService.updateDeviceData(deviceAcidityDto, id);
    if(device != null) {
      if(device.getActive()) {
        return ResponseEntity.ok(device);
      }
      return ResponseEntity.ok("DEVICE HAS BEEN DISABLED.");
    }
    return ResponseEntity.notFound().build();
  }
}
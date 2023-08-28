package com.service.zefu;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.service.zefu.dtos.UserDTO;
import com.service.zefu.enums.EnumRole;
import com.service.zefu.models.ProductModel;
import com.service.zefu.models.RoleModel;
import com.service.zefu.models.UserModel;
import com.service.zefu.services.AuthService;
import com.service.zefu.services.ProductService;
import com.service.zefu.services.RoleService;

@SpringBootApplication
public class ZefuApplication implements CommandLineRunner {

	private RoleService roleService;
	private AuthService authService;
	private ProductService productService;

	public ZefuApplication(RoleService roleService, AuthService authService, ProductService productService) {
		this.roleService = roleService;
		this.authService = authService;
		this.productService = productService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ZefuApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		RoleModel adminRole = new RoleModel();
		adminRole.setRole(EnumRole.ADMIN);

		RoleModel managerRole = new RoleModel();
		managerRole.setRole(EnumRole.MANAGER);

		RoleModel userRole = new RoleModel();
		userRole.setRole(EnumRole.USER);

		roleService.create(adminRole);
		roleService.create(managerRole);
		roleService.create(userRole);

		Date now = new Date();

		UserDTO userDTO = new UserDTO();
		userDTO.setName("Héuller Soares Vilela Silva");
		userDTO.setEmail("hllrdev@gmail.com");
		userDTO.setPassword("123456");
		//SET TO ADM AND PASS

		UserModel userModel = authService.signup(userDTO);

		ProductModel productModel = new ProductModel();
		productModel.setTitle("Jogo de colher, Medidor de Cozinha, Tramontina");
		productModel.setLink("https://amzn.to/47kAhJH");
		productModel.setPhoto("/images/medidor.jpg");

		productModel.setCreatedAt(now);
		productModel.setUpdatedAt(now);
		productModel.setUserModel(userModel);
		productService.save(productModel);

		ProductModel productModel2 = new ProductModel();
		productModel2.setTitle("Jogo de panelas c/ alça intercambiável, Tramontina");
		productModel2.setLink("https://amzn.to/3qiH1XR");
		productModel2.setPhoto("/images/panelas.jpg");

		productModel2.setCreatedAt(now);
		productModel2.setUpdatedAt(now);
		productModel2.setUserModel(userModel);
		productService.save(productModel2);

		ProductModel productModel3 = new ProductModel();
		productModel3.setTitle("Mixer Misturador Multiuso, M150-BR");
		productModel3.setLink("https://amzn.to/3s8WxGk");
		productModel3.setPhoto("/images/mixer.jpg");

		productModel3.setCreatedAt(now);
		productModel3.setUpdatedAt(now);
		productModel3.setUserModel(userModel);
		productService.save(productModel3);

		ProductModel productModel4 = new ProductModel();
		productModel4.setTitle("Organizador Inox Suprema, Brinox");
		productModel4.setLink("https://amzn.to/445s9tz");
		productModel4.setPhoto("/images/organizador.jpg");

		productModel4.setCreatedAt(now);
		productModel4.setUpdatedAt(now);
		productModel4.setUserModel(userModel);
		productService.save(productModel4);

		ProductModel productModel5 = new ProductModel();
		productModel5.setTitle("Tigela de cerâmica, Lyor");
		productModel5.setLink("https://amzn.to/442SIzt");
		productModel5.setPhoto("/images/tigela.jpg");

		productModel5.setCreatedAt(now);
		productModel5.setUpdatedAt(now);
		productModel5.setUserModel(userModel);
		productService.save(productModel5);
	}

}

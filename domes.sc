__command() -> 'root command';

circle(radius) -> (
  center_pos = pos(player());
  print('center ' + center_pos);

  radSq = radius ^ 2;

  c_for(x=0, x<radius, x+=1,
    c_for(z=0, z<radius, z+=1,
      r = x^2 + z^2;
      if(r <= radSq,
        // TODO: all 4 quadrants
        set(center_pos:0 + x, center_pos:1, center_pos:2 + z, 'glass')
      )
    )
  );
);

parabolic(radius, factor) -> (
  center_pos = pos(player());

  facSq = factor ^ 2;

  height = radius^2 / facSq;
  print('height ' + height);

  c_for(x=0, x<=radius, x+=1,
    c_for(z=0, z<=radius, z+=1,
      y = height - (x^2 / facSq + z^2 / facSq);
      if(y >= 0,
        for([1,-1],
          xmod = _;
          for([1, -1],
            zmod = _;
            set(center_pos:0 + x*xmod, center_pos:1 + y, center_pos:2 + z*zmod, 'glass')
          )
        )
      )
    )
  );
);

catenary(radius, factor) -> (
  center_pos = pos(player());

  height = cosh(radius / factor);
  print('height ' + height);

  c_for(x=0, x<=radius, x+=1,
    c_for(z=0, z<=radius, z+=1,
      y = height - cosh(sqrt(x^2 + z^2) / factor);
      if(y >= 0,
        for([1,-1],
          xmod = _;
          for([1, -1],
            zmod = _;
            set(center_pos:0 + x*xmod, center_pos:1 + y, center_pos:2 + z*zmod, 'glass')
          )
        )
      )
    )
  );
);
